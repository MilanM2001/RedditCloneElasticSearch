import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { FlairService } from 'src/app/services/flair.service';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  searchFormInputGroup: FormGroup = new FormGroup({
    search_input: new FormControl('')
  })

  searchFormNumbersGroup: FormGroup = new FormGroup({
    from: new FormControl(''),
    to: new FormControl('')
  })

  searchFormFlairGroup: FormGroup = new FormGroup({
    flair: new FormControl('')
  })

  searchTypeFormGroup: FormGroup = new FormGroup({
    search_type: new FormControl('')
  })

  optionFormGroup: FormGroup = new FormGroup({
    search_option: new FormControl('')
  })

  posts: Array<Post> = [];
  flairs: Array<Flair> = [];
  submittedSearch = false;
  submittedOption = false;
  submittedType = false;
  no_results = false;

  constructor(private postService: PostService,
    private flairService: FlairService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.searchFormInputGroup = this.formBuilder.group({
      search_input: ['', [Validators.required]]
    })

    this.searchFormNumbersGroup = this.formBuilder.group({
      from: ['', [Validators.required, Validators.min(0), Validators.max(1000)]],
      to: ['', [Validators.required, Validators.min(0), Validators.max(1000)]]
    })

    this.searchFormFlairGroup = this.formBuilder.group({
      flair: ['', [Validators.required]]
    })

    this.searchTypeFormGroup = this.formBuilder.group({
      search_type: ['', [Validators.required]]
    })

    this.optionFormGroup = this.formBuilder.group({
      search_option: ['', [Validators.required]]
    })

    this.postService.GetAllElastic()
      .subscribe({
        next: (data: Post[]) => {
          this.posts = data;
        },
        error: (error) => {
          console.log(error);
        }
      })

    this.flairService.GetAll()
      .subscribe({
        next: (data: Flair[]) => {
          this.flairs = data;
        },
        error: (error) => {
          console.log(error);
        }
      })
  }

  get searchInputGroup(): { [key: string]: AbstractControl } {
    return this.searchFormInputGroup.controls;
  }

  get searchNumbersGroup(): { [key: string]: AbstractControl } {
    return this.searchFormNumbersGroup.controls;
  }

  get searchFlairGroup(): { [key: string]: AbstractControl } {
    return this.searchFormFlairGroup.controls;
  }

  get searchTypeGroup(): { [key: string]: AbstractControl } {
    return this.searchTypeFormGroup.controls;
  }

  get optionGroup(): { [key: string]: AbstractControl } {
    return this.optionFormGroup.controls;
  }

  search() {
    this.submittedSearch = true;
    this.submittedOption = true;
    this.submittedType = true;

    let search_option;
    search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2) {
      if (this.searchFormInputGroup.invalid) {
        return;
      }
      if (this.searchTypeFormGroup.invalid) {
        return;
      }
    }

    if (search_option == 3 || search_option == 5) {
      if (this.searchFormNumbersGroup.invalid) {
        return;
      }
    }

    if (search_option == 4) {
      if (this.searchFormFlairGroup.invalid) {
        return;
      }
    }

    if (this.optionFormGroup.invalid) {
      return;
    }

    let search_input;
    search_input = this.searchFormInputGroup.get('search_input')?.value;

    let from;
    from = this.searchFormNumbersGroup.get('from')?.value;

    let to;
    to = this.searchFormNumbersGroup.get('to')?.value;

    let flair;
    flair = this.searchFormFlairGroup.get('flair')?.value;

    let searchType;
    searchType = this.searchTypeFormGroup.get('search_type')?.value;

    //Search by Title
    if (search_option == 1) {
      this.postService.GetAllByTitle(search_input, searchType)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          },
          error: (error) => {
            console.log(error);
          }
        })
    }

    //Search by Text
    if (search_option == 2) {
      this.postService.GetAllByText(search_input, searchType)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          },
          error: (error) => {
            console.log(error);
          }
        })
    }

    //Search by Number of Posts
    if (search_option == 3) {
      this.postService.GetAllByKarma(from, to)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          },
          error: (error) => {
            console.log(error);
          }
        })
    }

    //Search by Flair
    if (search_option == 4) {
      this.postService.GetAllByFlairName(flair)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          },
          error: (error) => {
            console.log(error);
          }
        })
    }

    //Search by Number of Comments
    if (search_option == 5) {
      this.postService.GetAllByNumberOfComments(from, to)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          },
          error: (error) => {
            console.log(error);
          }
        })
    }

  }

  isSearchingText(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2 || search_option == '') {
      return true;
    } else {
      return false
    }
  }

  isSearchingNumbers(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 3 || search_option == 5) {
      return true;
    } else {
      return false;
    }
  }

  isSearchingFlair(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 4) {
      return true;
    } else {
      return false;
    }
  }

}
