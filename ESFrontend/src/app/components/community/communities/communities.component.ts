import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-communities',
  templateUrl: './communities.component.html',
  styleUrls: ['./communities.component.css']
})
export class CommunitiesComponent implements OnInit {

  searchInputFormGroup: FormGroup = new FormGroup({
    search_input: new FormControl('')
  })

  searchNumbersFormGroup: FormGroup = new FormGroup({
    from: new FormControl(''),
    to: new FormControl('')
  })

  searchTypeFormGroup: FormGroup = new FormGroup({
    search_type: new FormControl('')
  })

  searchOptionFormGroup: FormGroup = new FormGroup({
    search_option: new FormControl('')
  })

  communities: Array<Community> = [];
  submittedSearch = false;
  submittedOption = false;
  submittedType = false;
  no_results = false;

  constructor(private communityService: CommunityService,
              private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.searchInputFormGroup = this.formBuilder.group({
      search_input: ['', [Validators.required]]
    })

    this.searchNumbersFormGroup = this.formBuilder.group({
      from: ['', [Validators.required, Validators.min(0), Validators.max(1000)]],
      to: ['', [Validators.required, Validators.min(0), Validators.max(1000)]]
    })

    this.searchTypeFormGroup = this.formBuilder.group({
      search_type: ['', [Validators.required]]
    })

    this.searchOptionFormGroup = this.formBuilder.group({
      search_option: ['', [Validators.required]]
    })

    this.communityService.GetAllElastic()
      .subscribe({
        next: (data: Community[]) => {
          this.communities = data;
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  get searchInputGroup(): { [key: string]: AbstractControl} {
    return this.searchInputFormGroup.controls;
  }

  get searchNumbersGroup(): { [key: string]: AbstractControl } {
    return this.searchNumbersFormGroup.controls;
  }

  get searchTypeGroup(): { [key: string]: AbstractControl } {
    return this.searchTypeFormGroup.controls;
  }

  get searchOptionGroup(): { [key: string]: AbstractControl } {
    return this.searchOptionFormGroup.controls;
  }

  search() {
    this.submittedSearch = true;
    this.submittedOption = true;
    this.submittedType = true;

    let search_option;
    search_option = this.searchOptionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2 || search_option == 3) {
      if (this.searchInputFormGroup.invalid) {
        return;
      }
      if (this.searchTypeFormGroup.invalid) {
        return;
      }
    }

    if (search_option == 4 || search_option == 5) {
      if (this.searchNumbersFormGroup.invalid) {
        return;
      }
    }

    if (this.searchOptionFormGroup.invalid) {
      return;
    }

    let search_input;
    search_input = this.searchInputFormGroup.get('search_input')?.value;

    let from;
    from = this.searchNumbersFormGroup.get('from')?.value;

    let to;
    to = this.searchNumbersFormGroup.get('to')?.value;

    let searchType;
    searchType = this.searchTypeFormGroup.get('search_type')?.value;

    //Search by Name
    if (search_option == 1) {
      this.communityService.GetAllByName(search_input, searchType)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
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

    //Search by Description
    if (search_option == 2) {
      this.communityService.GetAllByDescription(search_input, searchType)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
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
      this.communityService.GetAllByPDFDescription(search_input, searchType)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
            if (data.length == 0) {
              this.no_results == true;
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
    if (search_option == 4) {
      this.communityService.GetAllByNumberOfPosts(from, to)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
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

    if (search_option == 5) {
      this.communityService.GetAllByAverageKarma(from, to)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
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
    let search_option = this.searchOptionFormGroup.get('search_option')?.value;

    if (search_option == 1 || search_option == 2 || search_option == 3 || search_option == '') {
      return true;
    } else {
      return false
    }
  }

  isSearchingNumbers(): boolean {
    let search_option = this.searchOptionFormGroup.get('search_option')?.value;

    if (search_option == 4 || search_option == 5) {
      return true;
    } else {
      return false;
    }
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

}
