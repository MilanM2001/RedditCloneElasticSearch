import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AddPostDTO } from '../dto/addPostDTO';
import { Post } from '../model/post.model';
import { Comment } from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private url = "posts";
  constructor(private http: HttpClient) { }

  public GetAll(): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/all`);
  }

  public GetAllElastic(): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/allElastic`);
  }

  public GetSingle(post_id: number): Observable<Post> {
    return this.http.get<Post>(`${environment.baseApiUrl}/${this.url}/single/` + post_id);
  }

  public GetAllByTitle(title: String, searchType: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/findAllByTitle/` + title + '/' + searchType);
  }

  public GetAllByText(text: String, searchType: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/findAllByText/` + text + '/' + searchType);
  }

  public GetAllByPDFDescription(pdfDescription: String, searchType: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/findAllByPDFDescription/` + pdfDescription + '/' + searchType);
  }

  public GetAllByTwoFields(first: String, second: String, selected_fields: number, boolQueryType: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/findAllByTwoFields/` + first + '/' + second + '/' + selected_fields + '/' + boolQueryType);
  }

  public GetAllByKarma(from: String, to: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/karma/` + from + `/to/` + to);
  }

  public GetAllByFlairName(name: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/findAllByFlairName/` + name);
  }

  public GetAllByNumberOfComments(from: String, to: String): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/numberOfComments/` + from + `/to/` + to);
  }

  public GetPostComments(post_id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${environment.baseApiUrl}/${this.url}/comments/` + post_id);
  }

  public AddPost(community_id: number, postDTO: AddPostDTO): Observable<Post> {
    return this.http.post<Post>(`${environment.baseApiUrl}/${this.url}/add/` + community_id, postDTO);
  }

  public AddElasticPDF(uploadModel: FormData): Observable<Post> {
    return this.http.post<Post>(`${environment.baseApiUrl}/${this.url}/pdf`, uploadModel);
}

  public Update(post_id: number, post: Post) {
    return this.http.put(`${environment.baseApiUrl}/${this.url}/update/` + post_id, post);
  }

  public Delete(post_id: number) {
    return this.http.delete(`${environment.baseApiUrl}/${this.url}/delete/` + post_id);
  }
}