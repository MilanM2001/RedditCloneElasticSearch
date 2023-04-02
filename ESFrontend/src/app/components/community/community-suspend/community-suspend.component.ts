import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-community-suspend',
  templateUrl: './community-suspend.component.html',
  styleUrls: ['./community-suspend.component.css']
})
export class CommunitySuspendComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    suspended_reason: new FormControl('')
  });
  submitted = false;

  community_id: number = 0;
  community: Community = new Community();

  constructor(private communityService: CommunityService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router) { }

  ngOnInit(): void {
    this.community = new Community();

    this.formGroup = this.formBuilder.group({
      suspended_reason : ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]]
    });

    this.community_id = Number(this.route.snapshot.paramMap.get('community_id'));

    this.communityService.GetSingle(this.community_id)
      .subscribe({
        next: (data: Community) => {
          console.log(data);
          this.community = data;

          this.formGroup.get('suspended_reason')?.setValue(this.community.suspended_reason);
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  suspend() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.community.suspended_reason = this.formGroup.get('suspended_reason')?.value;

    this.communityService.Suspend(this.community_id, this.community)
      .subscribe({
        next: (data) => {
          console.log(data);
          this.router.navigate(['/Communities'])
        },
        error: (error) => {
          console.log(error);
        }
      })
  }

}
