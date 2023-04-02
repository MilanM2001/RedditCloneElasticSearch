import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDTO } from 'src/app/dto/loginDTO';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  formGroup: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  constructor(
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  submitted = false;

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    });
  }

  get loginGroup(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let login: LoginDTO = new LoginDTO();

    login.username = this.formGroup.get('username')?.value;
    login.password = this.formGroup.get('password')?.value;

    this.userService.login(login)
      .subscribe({
        next: (token: string) => {
          localStorage.setItem('authToken', token);
          this.router.navigateByUrl("/Communities");
        }
      })

  }

}
