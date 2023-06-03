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

  searchInputFormGroup: FormGroup = new FormGroup({
    search_input: new FormControl('')
  })

  searchTwoInputsFormGroup: FormGroup = new FormGroup({
    first: new FormControl(''),
    second: new FormControl(''),
    selected_fields: new FormControl(''),
    boolQueryType: new FormControl('')
  })

  searchNumbersFormGroup: FormGroup = new FormGroup({
    from: new FormControl(''),
    to: new FormControl('')
  })

  searchFlairFormGroup: FormGroup = new FormGroup({
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
  submittedTwoSearches = false
  submittedOption = false;
  submittedType = false;
  no_results = false;

  constructor(private postService: PostService,
    private flairService: FlairService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.searchInputFormGroup = this.formBuilder.group({
      search_input: ['', [Validators.required]]
    })

    this.searchTwoInputsFormGroup = this.formBuilder.group({
      first: ['', [Validators.required]],
      second: ['', [Validators.required]],
      selected_fields: ['', [Validators.required]],
      boolQueryType: ['', Validators.required]
    })

    this.searchNumbersFormGroup = this.formBuilder.group({
      from: ['', [Validators.required, Validators.min(0), Validators.max(1000)]],
      to: ['', [Validators.required, Validators.min(0), Validators.max(1000)]]
    })

    this.searchFlairFormGroup = this.formBuilder.group({
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
    return this.searchInputFormGroup.controls;
  }

  get searchTwoInputsGroup(): { [key: string]: AbstractControl } {
    return this.searchTwoInputsFormGroup.controls
  }

  get searchNumbersGroup(): { [key: string]: AbstractControl } {
    return this.searchNumbersFormGroup.controls;
  }

  get searchFlairGroup(): { [key: string]: AbstractControl } {
    return this.searchFlairFormGroup.controls;
  }

  get searchTypeGroup(): { [key: string]: AbstractControl } {
    return this.searchTypeFormGroup.controls;
  }

  get optionGroup(): { [key: string]: AbstractControl } {
    return this.optionFormGroup.controls;
  }

  search() {
    this.submittedSearch = true
    this.submittedOption = true
    this.submittedType = true
    this.submittedTwoSearches = true

    let search_option;
    search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2 || search_option == 3 || search_option == 4) {
      if (this.searchInputFormGroup.invalid) {
        return;
      }
      if (this.searchTypeFormGroup.invalid) {
        return;
      }
    }

    if (search_option == 5 || search_option == 6) {
      if (this.searchNumbersFormGroup.invalid) {
        return;
      }
    }

    if (search_option == 7) {
      if (this.searchFlairFormGroup.invalid) {
        return;
      }
    }

    if (search_option == 8) {
      if (this.searchTwoInputsFormGroup.invalid) {
        return
      }
      if (this.searchTypeFormGroup.invalid) {
        return
      }
    }

    if (this.optionFormGroup.invalid) {
      return;
    }

    let search_input;
    search_input = this.searchInputFormGroup.get('search_input')?.value;

    let from;
    from = this.searchNumbersFormGroup.get('from')?.value;

    let to;
    to = this.searchNumbersFormGroup.get('to')?.value;

    let flair;
    flair = this.searchFlairFormGroup.get('flair')?.value;

    let searchType;
    searchType = this.searchTypeFormGroup.get('search_type')?.value;

    let first
    first = this.searchTwoInputsFormGroup.get('first')?.value

    let second
    second = this.searchTwoInputsFormGroup.get('second')?.value

    let selected_fields
    selected_fields = this.searchTwoInputsFormGroup.get('selected_fields')?.value

    let boolQueryType
    boolQueryType = this.searchTwoInputsFormGroup.get('boolQueryType')?.value

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

    //Search by PDF Description
    if (search_option == 3) {
      this.postService.GetAllByPDFDescription(search_input, searchType)
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
            console.log(error)
          }
        })
    }

    //Search by Comment Text
    if (search_option == 4) {
      this.postService.GetAllByCommentText(search_input, searchType)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data
            if (data.length == 0) {
              this.no_results = true
            }
            if (data.length > 0) {
              this.no_results = false
            }
          },
          error: (error) => {
            console.log(error)
          }
        })
    }

    //Search by Number of Posts
    if (search_option == 5) {
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

    //Search by Number of Comments
    if (search_option == 6) {
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

    //Search by Flair
    if (search_option == 7) {
      this.postService.GetAllByFlairName(flair, "PHRASE")
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

    //Search by two fields
    if (search_option == 8) {
      console.log(first)
      console.log(second)
      console.log(selected_fields)
      console.log(boolQueryType)
      console.log(searchType)
      this.postService.GetAllByTwoFields(first, second, selected_fields, boolQueryType, searchType)
        .subscribe({
          next: (data: Post[]) => {
            this.posts = data
            if (data.length == 0) {
              this.no_results = true
            }
            if (data.length > 0) {
              this.no_results = false
            }
          },
          error: (error) => {
            console.log(error)
          }
        })
    }

  }
  
  isSearchingText(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2 || search_option == 3 || search_option == 4) {
      return true;
    } else {
      return false
    }
  }

  isSearchingNumbers(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 5 || search_option == 6) {
      return true;
    } else {
      return false;
    }
  }

  isSearchingFlair(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 7) {
      return true;
    } else {
      return false;
    }
  }

  isSearchingTwoTexts(): boolean {
    let search_option = this.optionFormGroup.get('search_option')?.value

    if (search_option == 8) {
      return true
    } else {
      return false
    }
  }

}
