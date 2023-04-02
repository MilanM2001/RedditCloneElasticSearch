import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from 'src/app/model/post.model';
import { User } from 'src/app/model/user.model';
import { PostService } from 'src/app/services/post.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css']
})
export class MyProfileComponent implements OnInit {

  user: User = new User();
  posts: Array<Post> = [];

  constructor(
  private userService: UserService,
  private postService: PostService,
  private router: Router
  ) { }

  ngOnInit(): void {
    this.userService.GetMe()
      .subscribe({
        next: (data) => {
          this.user = data;
          this.userService.GetUserPosts(this.user.user_id)
          .subscribe({
            next: (data: Post[]) => {
              this.posts = data;
            },
            error: (error: Error) => {
              console.log(error);
            }
          })
        },
        error: (error: Error) => {
          console.log(error);
        }
      })
  }

}
