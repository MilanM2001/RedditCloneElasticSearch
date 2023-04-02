import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-community-edit',
  templateUrl: './community-edit.component.html',
  styleUrls: ['./community-edit.component.css']
})
export class CommunityEditComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    description: new FormControl('')
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
      description : ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]]
    });

    this.community_id = Number(this.route.snapshot.paramMap.get('community_id'));

    this.communityService.GetSingle(this.community_id)
      .subscribe({
        next: (data: Community) => {
          console.log(data);
          this.community = data;

          this.formGroup.get('description')?.setValue(this.community.description);
        },
        error: (error) => {
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

    this.community.description = this.formGroup.get('description')?.value;

    this.communityService.Update(this.community_id, this.community)
      .subscribe({
        next: (data) => {
          console.log(data);
          this.router.navigate(['/Community-View', this.community_id])
        },
        error: (error) => {
          console.log(error);
        }
      })
  }

}
