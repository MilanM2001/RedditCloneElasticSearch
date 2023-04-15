import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AddCommentDTO } from 'src/app/dto/addCommentDTO';
import { CreateReactionDTO } from 'src/app/dto/createReactionDTO';
import { Comment } from 'src/app/model/comment.model';
import { Post } from 'src/app/model/post.model';
import { UserService } from 'src/app/services/user.service';
import { Reaction } from 'src/app/model/reaction.model';
import { ReactionType } from 'src/app/model/reactionType.enum';
import { User } from 'src/app/model/user.model';
import { CommentService } from 'src/app/services/comment.service';
import { PostService } from 'src/app/services/post.service';
import { ReactionService } from 'src/app/services/reaction.service';

@Component({
  selector: 'app-post-view',
  templateUrl: './post-view.component.html',
  styleUrls: ['./post-view.component.css']
})
export class PostViewComponent implements OnInit {

  constructor(
  private postService: PostService,
  private reactionService: ReactionService,
  private route: ActivatedRoute,
  private router: Router,
  private formBuilder: FormBuilder,
  private commentService: CommentService,
  private userService: UserService) 
  { }

  commentFormGroup: FormGroup = new FormGroup({
    text: new FormControl('')
  });

  post: Post = new Post();
  loggedInUser: User = new User();
  comments: Comment[] = [];
  post_id = Number(this.route.snapshot.paramMap.get('post_id'))
  community_id: Number = 0;
  post_karma: number = 0;
  submittedComment: boolean = false;

  ngOnInit(): void {
    this.postService.GetSingle(this.post_id)
      .subscribe({
        next: (data: Post) => {
          this.post = data as Post;
          this.community_id = this.post.community.community_id
        },
        error: (error) => {
          console.log(error);  	
        }
      })

    this.reactionService.postKarma(this.post_id)
    .subscribe({
        next: (data: number) => {
            this.post_karma = data;
        },
        error: (error: Error) => {
          console.log(error);
        }
    })

    this.postService.GetPostComments(this.post_id)
      .subscribe({
        next: (data) => {
          this.comments = data;
        },
        error: (error) => {
          console.log(error);
        }
      })

    this.userService.GetMe()
      .subscribe({
        next: (data) => {
          this.loggedInUser = data;
        },
        error: (error) => {
          console.log(error);
        }
      })

    this.commentFormGroup = this.formBuilder.group({
        text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(200)]]
    })
  }

  get commentGroup(): { [key: string]: AbstractControl } {
    return this.commentFormGroup.controls;
  }

  upvotePost() {
      let reaction = new CreateReactionDTO();
      reaction.reaction_type = ReactionType[ReactionType.UPVOTE];
      reaction.post_id = this.post.post_id;
      this.reactionService.create(reaction)
        .subscribe({
          next: (data: Reaction) => {
          },
          error: (error: Error) => {
            console.log(error);
          },
          complete: () => {
            this.reactionService.postKarma(this.post_id)
              .subscribe({
                  next: (data: number) => {
                      this.post_karma = data;
                  },
                  error: (error: Error) => {
                    console.log(error);
                  }
              })
          }
        })
  }

  downvotePost() {
    let reaction = new CreateReactionDTO();
    reaction.reaction_type = ReactionType[ReactionType.DOWNVOTE];
    reaction.post_id = this.post.post_id;
    this.reactionService.create(reaction)
      .subscribe({
        next: (data: Reaction) => {
        },
        error: (error: Error) => {
          console.log(error);
        },
        complete: () => {
          this.reactionService.postKarma(this.post_id)
            .subscribe({
                next: (data: number) => {
                    this.post_karma = data;
                },
                error: (error: Error) => {
                  console.log(error);
                }
            })
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

  addComment() {
    this.submittedComment = true;

    if (this.commentFormGroup.invalid) {
      return;
    }

    let addComment: AddCommentDTO = new AddCommentDTO();

    addComment.text = this.commentFormGroup.get('text')?.value;

    this.commentService.AddComment(this.post_id, addComment)
      .subscribe({
        next: (data) => {
          this.commentFormGroup.controls['text'].reset();
        },
        error: (error) => {
          console.log(error);
        },
        complete: () => {
          this.postService.GetPostComments(this.post_id)
            .subscribe({
              next: (data) => {
                this.comments = data;
              },
              error: (error) => {
                console.log(error);
              }
            })
        }
      })
  }

  isLoggedIn(): boolean {
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

  isByUser(): boolean {
    if (this.post.user.username == this.loggedInUser.username) {
      return true;
    }
    else {
      return false;
    }
  }

}
