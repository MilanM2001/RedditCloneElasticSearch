import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-view',
  templateUrl: './post-view.component.html',
  styleUrls: ['./post-view.component.css']
})
export class PostViewComponent implements OnInit {

  post: Post = new Post();

  constructor(
  private postService: PostService,
  private route: ActivatedRoute,
  private router: Router) { }

  post_id = Number(this.route.snapshot.paramMap.get('post_id'))
  community_id: Number = 0;

  ngOnInit(): void {
    this.postService.GetSingle(this.post_id)
      .subscribe({
        next: (data: Post) => {
          this.post = data as Post;
          this.community_id = this.post.community.community_id
          console.log(this.post);
        },
        error: (error) => {
          console.log(error);  	
        }
      })
  }

  deletePost() {
    if(window.confirm("Are you sure you want to delete this post?")) {
      this.postService.Delete(this.post_id)
        .subscribe({
          next: () => {
            this.router.navigate(['/Community-View', this.community_id])
          },
          error: (error) => {
            console.log(error);
          }
        });
    };
  }

  public isLoggedIn(): boolean {
    if (localStorage.getItem("authToken") != null) {
      return true;
    }
    else {
      return false;
    }
  }

  notLoggedIn(): boolean {
    if (localStorage.getItem("authToken") === null) {
      return true
    }
    else {
      return false
    }
  }

}
