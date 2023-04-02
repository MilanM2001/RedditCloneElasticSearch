import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.css']
})
export class PostEditComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    title: new FormControl(''),
    text: new FormControl('')
  })
  submitted = false;

  post_id = Number(this.route.snapshot.paramMap.get('post_id'));
  post: Post = new Post();

  constructor(
  private postService: PostService,
  private route: ActivatedRoute,
  private formBuilder: FormBuilder,
  private router: Router) { }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      title : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(35)]],
      text : ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]]
    });

    this.postService.GetSingle(this.post_id)
      .subscribe({
        next: (data: Post) => {
          console.log(data);
          this.post = data;

          this.formGroup.get('title')?.setValue(this.post.title);
          this.formGroup.get('text')?.setValue(this.post.text);
        },
        error: (error: Error) => {
          console.log(error);
        }
      });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  update() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.post.title = this.formGroup.get('title')?.value;
    this.post.text = this.formGroup.get('text')?.value;

    this.postService.Update(this.post_id, this.post)
      .subscribe({
        next: (data: any) => {
          console.log(data);
          this.router.navigate(['/Post-View', this.post_id])
        },
        error: (error: Error) => {
          console.log(error);
        }
      })
  }

}
