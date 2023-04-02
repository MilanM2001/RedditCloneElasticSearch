import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Rule } from 'src/app/model/rule.model';
import { RuleService } from 'src/app/services/rule.service';

@Component({
  selector: 'app-rule-edit',
  templateUrl: './rule-edit.component.html',
  styleUrls: ['./rule-edit.component.css']
})
export class RuleEditComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    name: new FormControl(''),
    description: new FormControl('')
  })
  submitted = false;

  rule_id = Number(this.route.snapshot.paramMap.get('rule_id'));
  rule: Rule = new Rule();

  constructor(private ruleService: RuleService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router) { }

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      name : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      description : ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]]
    });

    this.ruleService.GetSingle(this.rule_id)
      .subscribe({
        next: (data: Rule) => {
          this.rule = data;

          this.formGroup.get('name')?.setValue(this.rule.name);
          this.formGroup.get('description')?.setValue(this.rule.description);
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

  delete() {
    if(window.confirm("Are you sure you want to delete this rule?")) {
      this.ruleService.Delete(this.rule_id)
        .subscribe({
          next: () => {
            console.log("Rule Deleted");
          },
          error: (error) => {
            console.log(error);
          }
        });
    };
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  update() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.rule.name = this.formGroup.get('name')?.value;
    this.rule.description = this.formGroup.get('description')?.value;

    this.ruleService.Update(this.rule_id, this.rule)
      .subscribe({
        next: () => {
          this.router.navigate(['/Communities'])
        },
        error: (error) => {
          console.log(error);
        }
      })
  }

}
