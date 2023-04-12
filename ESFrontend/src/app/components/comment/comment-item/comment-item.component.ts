import { Component, Input, OnInit } from '@angular/core';
import { Comment } from 'src/app/model/comment.model';
import { CommentService } from 'src/app/services/comment.service';

@Component({
  selector: 'app-comment-item',
  templateUrl: './comment-item.component.html',
  styleUrls: ['./comment-item.component.css']
})
export class CommentItemComponent implements OnInit {

  @Input() comment: Comment = new Comment();

  constructor(private commentService: CommentService) { }

  ngOnInit(): void {
  }

  deletePost() {
    if(window.confirm("Are you sure you want to delete this post?")) {
      this.commentService.Delete(this.comment.comment_id)
        .subscribe({
          next: () => {
            
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

}
