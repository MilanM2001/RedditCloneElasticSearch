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

  searchFormGroup: FormGroup = new FormGroup({
    search_input: new FormControl(''),
    from: new FormControl(''),
    to: new FormControl('')
  })

  optionFormGroup: FormGroup = new FormGroup({
    search_option: new FormControl('')
  })

  communities: Array<Community> = [];
  submittedSearch = false;
  submittedOption = false;
  no_results = false;

  constructor(private communityService: CommunityService,
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

    this.communityService.GetAll()
      .subscribe({
        next: (data: Community[]) => {
          this.communities = data;
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  get searchGroup(): { [key: string]: AbstractControl} {
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

    //Search by Name
    if (search_option == 1) {
      this.communityService.GetAllByName(search_input)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          }
        })
    }

    //Search by Description
    if (search_option == 2) {
      this.communityService.GetAllByDescription(search_input)
        .subscribe({
          next: (data: Community[]) => {
            this.communities = data;
            if (data.length == 0) {
              this.no_results = true;
            }
            if (data.length > 0) {
              this.no_results = false;
            }
          }
        })
      }

    //Search by Number of Posts
    if (search_option == 3) {
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
          }
        })
    }

  }



  isSearchingText(): boolean {
    let search_option;
    search_option = this.optionFormGroup.get('search_option')?.value;

    if (search_option == 3) {
      return false;
    } else if (search_option == 1 || search_option == 2){
      return true;
    }

    return true;
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
