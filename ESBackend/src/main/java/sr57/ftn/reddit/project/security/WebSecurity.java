package sr57.ftn.reddit.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sr57.ftn.reddit.project.service.UserService;

@Component
public class WebSecurity {

    @Autowired
    private UserService userService;



    /*public boolean checkClubId(Authentication authentication, HttpServletRequest request, int id) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        if(id == user.getId()) {
            return true;
        }
        return false;
    }*/
}
