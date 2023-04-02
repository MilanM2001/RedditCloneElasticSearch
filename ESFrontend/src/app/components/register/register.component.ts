import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AddPostDTO } from 'src/app/dto/addPostDTO';
import { RegisterDTO } from 'src/app/dto/registerDTO';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { User } from 'src/app/model/user.model';
import { CommunityService } from 'src/app/services/community.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerGroup: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
    email: new FormControl(''),
    display_name: new FormControl('')
  });

  constructor(
  private userService: UserService,
  private router: Router,
  private formBuilder: FormBuilder) { }
  
  submitted = false;
  
  ngOnInit(): void {
      this.registerGroup = this.formBuilder.group({
        username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]],
        password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]],
        email: ['', [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(30)]],
        display_name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]]
      })
  }

  get f(): { [key: string]: AbstractControl } {
    return this.registerGroup.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerGroup.invalid) {
      return;
    }

    let register: RegisterDTO = new RegisterDTO();

    register.username = this.registerGroup.get('username')?.value;
    register.password = this.registerGroup.get('password')?.value;
    register.email = this.registerGroup.get('email')?.value;
    register.display_name = this.registerGroup.get('display_name')?.value;

    this.userService.register(register)
      .subscribe({
        next: (data: User) => {
          console.log(data);
          this.router.navigateByUrl("/Login")
        },
        error: (error: Error) => {
          console.log(error);
        }
      })
  }

}
