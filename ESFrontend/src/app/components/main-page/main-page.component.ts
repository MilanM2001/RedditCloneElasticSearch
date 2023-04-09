import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  searchFormGroup: FormGroup = new FormGroup({
    search_input: new FormControl(''),
    from: new FormControl(''),
    to: new FormControl('')
  })

  optionFormGroup: FormGroup = new FormGroup({
    search_option: new FormControl('')
  })

  posts: Array<Post> = [];
  submittedSearch = false;
  submittedOption = false;
  no_results = false;

  constructor(private postService: PostService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.searchFormGroup = this.formBuilder.group({
      search_input: ['', [Validators.required]],
      from: ['', [Validators.min(0), Validators.max(1000)]],
      to: ['', [Validators.min(0), Validators.max(1000)]]
    })

    this.optionFormGroup = this.formBuilder.group({
      search_option: ['', [Validators.required]]
    })

    this.postService.GetAllElastic()
      .subscribe({
        next: (data: Post[]) => {
          this.posts = data;
        },
        error: (error: Error) => {
          console.log(error);
        }
      })
  }

  get searchGroup(): { [key: string]: AbstractControl } {
    return this.searchFormGroup.controls;
  }

  get optionGroup(): { [key: string]: AbstractControl } {
    return this.optionFormGroup.controls;
  }

  search() {
    this.submittedSearch = true;
    this.submittedOption = true;

    let search_option;
    search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option != 3) {
      if (this.searchFormGroup.invalid) {
        return;
      }
    }

    if (this.optionFormGroup.invalid) {
      return;
    }

    let search_input;
    search_input = this.searchFormGroup.get('search_input')?.value;

    let from;
    from = this.searchFormGroup.get('from')?.value;

    let to;
    to = this.searchFormGroup.get('to')?.value;

    //Search by Title
    if (search_option == 1) {
      this.postService.GetAllByTitle(search_input)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          }
        })
    }

    //Search by Text
    if (search_option == 2) {
      this.postService.GetAllByText(search_input)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          }
        })
      }

    // //Search by Number of Posts
    // if (search_option == 3) {
    //   this.communityService.GetAllByNumberOfPosts(from, to)
    //     .subscribe({
    //       next: (data: Community[]) => {
    //         this.communities = data;
    //         if (data.length == 0) {
    //           this.no_results = true;
    //         }
    //         if (data.length > 0) {
    //           this.no_results = false;
    //         }
    //       }
    //     })
    // }

  }

  isSearchingText(): boolean {
    let search_option;
    search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 3) {
      return false;
    } else if (search_option == 1 || search_option == 2) {
      return true;
    }

    return true;
  }

}
