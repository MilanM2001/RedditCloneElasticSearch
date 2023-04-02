import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AddPostDTO } from '../dto/addPostDTO';
import { Post } from '../model/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private url = "posts";
  constructor(private http: HttpClient) { }

  public GetAll(): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/all`);
  }

  public GetSingle(post_id: number): Observable<Post> {
    return this.http.get<Post>(`${environment.baseApiUrl}/${this.url}/single/` + post_id);
  }

  public AddPost(community_id: number, postDTO: AddPostDTO): Observable<Post> {
    return this.http.post<Post>(`${environment.baseApiUrl}/${this.url}/add/` + community_id, postDTO);
  }

  public Update(post_id: number, post: Post) {
    return this.http.put(`${environment.baseApiUrl}/${this.url}/update/` + post_id, post);
  }

  public Delete(post_id: number) {
    return this.http.delete(`${environment.baseApiUrl}/${this.url}/delete/` + post_id);
  }
}