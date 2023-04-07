package sr57.ftn.reddit.project.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sr57.ftn.reddit.project.model.dto.postDTOs.PostDTO;
import sr57.ftn.reddit.project.model.dto.userDTOs.*;
import sr57.ftn.reddit.project.model.entity.Post;
import sr57.ftn.reddit.project.model.entity.User;
import sr57.ftn.reddit.project.security.JwtAuthenticationRequest;
import sr57.ftn.reddit.project.security.TokenUtils;
import sr57.ftn.reddit.project.service.PostService;
import sr57.ftn.reddit.project.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/users")
public class UserController {
    final UserService userService;
    final PostService postService;
    final ModelMapper modelMapper;
    final AuthenticationManager authenticationManager;
    final UserDetailsService userDetailsService;
    final TokenUtils tokenUtils;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PostService postService, AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService, TokenUtils tokenUtils,
                          ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.postService = postService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> GetAll() {
        List<User> users = userService.findAll();

        List<UserDTO> usersDTO = modelMapper.map(users, new TypeToken<List<UserDTO>>() {
        }.getType());
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping("/single/{user_id}")
    @CrossOrigin
    public ResponseEntity<UserDTO> GetUser(@PathVariable("user_id") Integer user_id) {
        User user = userService.findOne(user_id);
        return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/getMe")
    @CrossOrigin
    public ResponseEntity<UserDTO> GetMe(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(modelMapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping(value = "/posts/{user_id}")
    @CrossOrigin
    public ResponseEntity<List<PostDTO>> GetUserPosts(@PathVariable Integer user_id) {
        List<Post> posts = postService.findPostsByUserId(user_id);

        List<PostDTO> postsDTO = modelMapper.map(posts, new TypeToken<List<PostDTO>>() {}.getType());
        return new ResponseEntity<>(postsDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = "application/json")
    @CrossOrigin
    public ResponseEntity<AddUserDTO> Register(@RequestBody AddUserDTO addUserDTO) {

        User newUser = new User();

        newUser.setUsername(addUserDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));
        newUser.setEmail(addUserDTO.getEmail());
        newUser.setAvatar("https://www.redditinc.com/assets/images/galleries/snoo-small.png");
        newUser.setRegistration_date(LocalDate.now());
        newUser.setDescription("No Description");
        newUser.setDisplay_name(addUserDTO.getDisplay_name());
        newUser.getRole();

        Optional<User> existingUsername = userService.findFirstByUsername(addUserDTO.getUsername());
        Optional<User> existingEmail = userService.findFirstByEmail(addUserDTO.getEmail());

        if (existingUsername.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (existingEmail.isPresent()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        newUser = userService.save(newUser);
        return new ResponseEntity<>(modelMapper.map(newUser, AddUserDTO.class), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity<String> Login(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(jwt);
    }

    @PutMapping(value = "/updatePassword/{user_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdatePasswordDTO> UpdatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @PathVariable("user_id") Integer user_id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User user = userService.findOne(user_id);

        if (currentUser.getUser_id() != user.getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        if (passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
            user = userService.save(user);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(modelMapper.map(user, UpdatePasswordDTO.class), HttpStatus.OK);
    }

    @PutMapping(value = "/updateInfo/{user_id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @CrossOrigin
    public ResponseEntity<UpdateInfoDTO> UpdateInfo(@RequestBody UpdateInfoDTO updateInfoDTO, @PathVariable("user_id") Integer user_id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        User user = userService.findOne(user_id);

        if (currentUser.getUser_id() != user.getUser_id()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        user.setDisplay_name(updateInfoDTO.getDisplay_name());
        user.setDescription(updateInfoDTO.getDescription());

        user = userService.save(user);

        return new ResponseEntity<>(modelMapper.map(user, UpdateInfoDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> DeleteUser(@PathVariable("user_id") Integer user_id) {
        User user = userService.findOne(user_id);

        if (user != null) {
            userService.remove(user_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //ANDROID


    @PostMapping("/loginAndroid")
    public ResponseEntity<UserDTO> loginAndroid(@RequestBody @Validated LoginDTO userDto) {

        User user = userService.findByUsername(userDto.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        //if credentials are correct
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //so we know who is loggedIn
        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
            User user1 = userService.findByUsername(userDto.getUsername());
            UserDTO userDTO = new UserDTO(user1);
            userDTO.setToken(tokenUtils.generateToken(userDetails));
            return ResponseEntity.ok(userDTO);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/myInfo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @CrossOrigin
    public ResponseEntity<UserDTO> getMyInfo(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String username = userPrincipal.getUsername();
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK);
    }

    @PutMapping(value = "/updateUserAndroid")
    public ResponseEntity<UpdateInfoDTO> updateInfo(@RequestBody UpdateInfoDTO updateInfoDTO, Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String username = userPrincipal.getUsername();
        User user = userService.findByUsername(username);

        user.setDisplay_name(updateInfoDTO.getDisplay_name());
        user.setDescription(updateInfoDTO.getDescription());

        userService.save(user);

        return new ResponseEntity<>(modelMapper.map(user, UpdateInfoDTO.class), HttpStatus.OK);
    }

    @PostMapping(value = "/updatePasswordAndroid")
    public ResponseEntity<Boolean> updatePasswordAndroid(@RequestBody @Validated UpdatePasswordAndroidDTO updatePasswordAndroidDTO, Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String username = userPrincipal.getUsername();
        User user = userService.findByUsername(username);

        boolean matches = BCrypt.checkpw(updatePasswordAndroidDTO.getOldPassword(), user.getPassword());
        if (!matches) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        user.setPassword(passwordEncoder.encode(updatePasswordAndroidDTO.getNewPassword()));

        userService.save(user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}