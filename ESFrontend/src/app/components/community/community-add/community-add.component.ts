import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AddCommunityDTO } from 'src/app/dto/addCommunityDTO';
import { Flair } from 'src/app/model/flair.model';
import { Rule } from 'src/app/model/rule.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-community-add',
  templateUrl: './community-add.component.html',
  styleUrls: ['./community-add.component.css']
})
export class CommunityAddComponent implements OnInit {

  communityFormGroup: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl('')
  });

  ruleFormGroup: FormGroup = new FormGroup({
    ruleName: new FormControl(''),
    ruleDescription: new FormControl('')
  });

  flairFormGroup: FormGroup = new FormGroup({
    flairName: new FormControl('')
  })

  rules: Rule[] = [];
  flairs: Flair[] = [];

  constructor(private communityService: CommunityService,
              private formBuilder: FormBuilder,
              private router: Router) { }

  submittedCommunity = false;
  submittedRule = false;
  submittedFlair = false;

  ngOnInit(): void {
    this.communityFormGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(300)]]
    });

    this.ruleFormGroup = this.formBuilder.group({
      ruleName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      ruleDescription: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]]
    });

    this.flairFormGroup = this.formBuilder.group({
      flairName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]]
    })
  }

  addRule() {
    this.submittedRule = true;

    if (this.ruleFormGroup.invalid) {
      return;
    }

    let addRule: Rule = new Rule();
    addRule.name = this.ruleFormGroup.get('ruleName')?.value;
    addRule.description = this.ruleFormGroup.get('ruleDescription')?.value;
    this.rules.push(addRule);
    this.ruleFormGroup.controls['ruleName'].reset();
    this.ruleFormGroup.controls['ruleDescription'].reset();
  }

  addFlair() {
    this.submittedFlair = true;
    
    if (this.flairFormGroup.invalid) {
      return;
    }

    let addFlair: Flair = new Flair();
    addFlair.name = this.flairFormGroup.get('flairName')?.value;
    this.flairs.push(addFlair);
    this.flairFormGroup.controls['flairName'].reset();
  }

  get communityGroup(): { [key: string]: AbstractControl } {
    return this.communityFormGroup.controls;
  }

  get ruleGroup(): { [key: string]: AbstractControl } {
    return this.ruleFormGroup.controls;
  }

  get flairGroup(): { [key: string]: AbstractControl} {
    return this.flairFormGroup.controls;
  }

  onSubmit() {
    this.submittedCommunity = true;

    if (this.communityFormGroup.invalid) {
      return;
    }

    let addCommunity: AddCommunityDTO = new AddCommunityDTO();
    let uploadModel: FormData = new FormData();
    let newCommunity_id: number;

    addCommunity.name = this.communityFormGroup.get('name')?.value;
    addCommunity.description = this.communityFormGroup.get('description')?.value;
    addCommunity.rules = this.rules;
    addCommunity.flairs = this.flairs;

    this.communityService.AddCommunity(addCommunity)
      .subscribe({
        next: (data) => {
          newCommunity_id = data.community_id;
        },
        error: (error) => {
          console.log(error);
        }, complete: () => {

          if (this.event != undefined) {
            const file: File = this.event.target.files[0];
            uploadModel.append("community_id", String(newCommunity_id))
            uploadModel.append("name", addCommunity.name);
            uploadModel.append("description", addCommunity.description);
            uploadModel.append("files", file);
            
            this.communityService.AddElasticPDF(uploadModel)
              .subscribe({
                next: (data) => {
                  console.log("ADDED WITH PDF");
                  this.router.navigateByUrl("/Communities");
                },
                error: (error) => {
                  console.log(error);
                }
              })
          } else {
            uploadModel.append("community_id", String(newCommunity_id))
            uploadModel.append("name", addCommunity.name);
            uploadModel.append("description", addCommunity.description);
            //Add List, test later
            // this.flairs.forEach(flair => {
            //   uploadModel.append("flairs", String(flair))
            // });
            this.communityService.AddElasticPDF(uploadModel)
              .subscribe({
                next: (data) => {
                  console.log("ADDED WITHOUT PDF");
                  this.router.navigateByUrl("/Communities");
                },
                error: (error) => {
                  console.log(error);
                }
              })
            }
        }
      })
  }

  setEvent(event: any) {
    this.event = event;
    console.log(this.event)
  }

  event: any;

}
