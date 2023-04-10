import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateReactionDTO } from 'src/app/dto/createReactionDTO';
import { Post } from 'src/app/model/post.model';
import { Reaction } from 'src/app/model/reaction.model';
import { ReactionType } from 'src/app/model/reactionType.enum';
import { PostService } from 'src/app/services/post.service';
import { ReactionService } from 'src/app/services/reaction.service';

@Component({
  selector: 'app-post-view',
  templateUrl: './post-view.component.html',
  styleUrls: ['./post-view.component.css']
})
export class PostViewComponent implements OnInit {

  post: Post = new Post();

  constructor(
  private postService: PostService,
  private reactionService: ReactionService,
  private route: ActivatedRoute,
  private router: Router) { }

  post_id = Number(this.route.snapshot.paramMap.get('post_id'))
  community_id: Number = 0;
  post_karma: number = 0;

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
