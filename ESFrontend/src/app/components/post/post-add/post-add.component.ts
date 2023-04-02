import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AddPostDTO } from 'src/app/dto/addPostDTO';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { CommunityService } from 'src/app/services/community.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-add',
  templateUrl: './post-add.component.html',
  styleUrls: ['./post-add.component.css']
})
export class PostAddComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    title: new FormControl(''),
    text: new FormControl(''),
    flairId: new FormControl('')
  });

  community: Community = new Community();
  community_id = Number(this.route.snapshot.paramMap.get('community_id'))
  flairs: Flair[] =  [];

  constructor(private communityService: CommunityService,
              private postService: PostService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder) { }
  
  submitted = false;
  
  ngOnInit(): void {
    this.communityService.GetSingle(this.community_id)
      .subscribe({
        next: (data: Community) => {
          this.community = data as Community;
          console.log(this.community);
        },
        error: (error) => {
          console.log(error);
        }
      });

    this.communityService.GetCommunityFlairs(this.community_id)
      .subscribe({
        next: (data: Flair[]) => {
          this.flairs = data as Flair[];
          console.log(this.flairs);
        }
      })

      this.formGroup = this.formBuilder.group({
        title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(35)]],
        text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]],
        flair_id: ['', [Validators.required]]
      })
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let addPost: AddPostDTO = new AddPostDTO();

    addPost.title = this.formGroup.get('title')?.value;
    addPost.text = this.formGroup.get('text')?.value;
    addPost.flair_id = this.formGroup.get('flair_id')?.value;

    this.postService.AddPost(this.community_id, addPost)
      .subscribe({
        next: (data: Post) => {
          console.log(data);
          history.back();
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

}
