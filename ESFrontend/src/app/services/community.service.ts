import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { AddCommunityDTO } from "../dto/addCommunityDTO";
import { Community } from "../model/community.model";
import { Flair } from "../model/flair.model";
import { Post } from "../model/post.model";
import { Rule } from "../model/rule.model";

@Injectable({
    providedIn: 'root'
})
export class CommunityService {
    private url = "communities";
    constructor(private http: HttpClient) { }

    public GetAll(): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/all`);
    }

    public GetAllElastic(): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/allElastic`);
    }

    public GetSingle(community_id: number): Observable<Community> {
        return this.http.get<Community>(`${environment.baseApiUrl}/${this.url}/single/` + community_id);
    }

    public GetAllByName(name: String, searchType: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/findAllByName/` + name + '/' + searchType);
    }

    public GetAllByDescription(description: String, searchType: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/findAllByDescription/` + description + '/' + searchType);
    }

    public GetAllByPDFDescription(pdfDescription: String, searchType: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/findAllByPDFDescription/` + pdfDescription + '/' + searchType);
    }

    public GetAllByRulesDescription(description: String, searchType: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/findAllByRulesDescription/` + description + '/' + searchType);
    }

    public GetAllByTwoFields(first: String, second: String, selected_fields: number, boolQueryType: String, searchType: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/findAllByTwoFields/` + first + '/' + second + '/' + selected_fields + '/' + boolQueryType + '/' + searchType);
    }

    public GetAllByNumberOfPosts(from: String, to: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/numberOfPosts/` + from + `/to/` + to)
    }

    public GetAllByAverageKarma(from: String, to: String): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/averageKarma/` + from + `/to/` + to)
    }

    public GetCommunityPosts(community_id: number): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/posts/` + community_id);
    }

    public GetCommunityElasticPostsByName(name: string): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/elasticPosts/` + name);
    }

    public GetCommunityFlairs(community_id: number): Observable<Flair[]> {
        return this.http.get<Flair[]>(`${environment.baseApiUrl}/${this.url}/flairs/` + community_id);
    }

    public GetCommunityRules(community_id: number): Observable<Rule[]> {
        return this.http.get<Rule[]>(`${environment.baseApiUrl}/${this.url}/rules/` + community_id);
    }

    public AddCommunity(addCommunityDTO: AddCommunityDTO): Observable<Community> {
        return this.http.post<Community>(`${environment.baseApiUrl}/${this.url}/add`, addCommunityDTO);
    }

    public AddElasticPDF(uploadModel: FormData): Observable<Community> {
        return this.http.post<Community>(`${environment.baseApiUrl}/${this.url}/pdf`, uploadModel);
    }

    public Update(community_id: number, community: Community) {
        return this.http.put(`${environment.baseApiUrl}/${this.url}/update/` + community_id, community);
    }

    public Suspend(community_id: number, community: Community) {
        return this.http.put(`${environment.baseApiUrl}/${this.url}/suspend/` + community_id, community);
    }
}