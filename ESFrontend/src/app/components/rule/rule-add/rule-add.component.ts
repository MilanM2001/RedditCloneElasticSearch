import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AddRuleDTO } from 'src/app/dto/addRuleDTO';
import { Community } from 'src/app/model/community.model';
import { Rule } from 'src/app/model/rule.model';
import { CommunityService } from 'src/app/services/community.service';
import { RuleService } from 'src/app/services/rule.service';

@Component({
  selector: 'app-rule-add',
  templateUrl: './rule-add.component.html',
  styleUrls: ['./rule-add.component.css']
})
export class RuleAddComponent implements OnInit {

  ruleFormGroup: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl('')
  });

  community_id: number = Number(this.route.snapshot.paramMap.get("community_id"));
  community: Community = new Community();

  constructor(private communityService: CommunityService,
              private ruleService: RuleService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router) { }

  submittedRule = false;

  ngOnInit(): void {
    this.ruleFormGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]]
    });

    this.communityService.GetSingle(this.community_id)
      .subscribe({
        next: (data: Community) => {
          console.log(data);
          this.community = data;
        },
        error: (error) => {
          console.error(error);
        }
      })
  }

  get ruleGroup(): { [key: string]: AbstractControl } {
    return this.ruleFormGroup.controls;
  }

  onSubmit() {
    this.submittedRule = true;

    if (this.ruleFormGroup.invalid) {
      return;
    }

    let addRule: AddRuleDTO = new AddRuleDTO();

    addRule.name = this.ruleFormGroup.get('name')?.value;
    addRule.description = this.ruleFormGroup.get('description')?.value;

    this.ruleService.AddRule(this.community_id, addRule)
      .subscribe({
        next: (data: Rule) => {
          this.router.navigate(['/View-Community' + this.community_id])
        },
        error: (error) => {
          console.log(error);
        }
      })


  }

}
