import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AddRuleDTO } from '../dto/addRuleDTO';
import { Rule } from '../model/rule.model';

@Injectable({
  providedIn: 'root'
})
export class RuleService {
  private url = "rules";
  
  constructor(private http: HttpClient) { }

  public GetSingle(rule_id: number): Observable<Rule> {
    return this.http.get<Rule>(`${environment.baseApiUrl}/${this.url}/single/` + rule_id);
  }

  public AddRule(community_id: number, addRuleDTO: AddRuleDTO): Observable<Rule> {
    return this.http.post<Rule>(`${environment.baseApiUrl}/${this.url}/add/` + community_id, addRuleDTO)
  }

  public Update(rule_id: number, rule: Rule) {
    return this.http.put<Rule>(`${environment.baseApiUrl}/${this.url}/update/` + rule_id, rule);
  }

  public Delete(rule_id: number) {
    return this.http.delete(`${environment.baseApiUrl}/${this.url}/` + rule_id);
  }
}