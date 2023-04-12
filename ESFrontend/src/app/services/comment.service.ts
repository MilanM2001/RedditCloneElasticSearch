import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddCommentDTO } from '../dto/addCommentDTO';
import { Observable } from 'rxjs';
import { Comment } from '../model/comment.model';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    private url = "comments";
    constructor(private http: HttpClient) {}

    public AddComment(post_id: number, commentDTO: AddCommentDTO): Observable<Comment> {
        return this.http.post<Comment>(`${environment.baseApiUrl}/${this.url}/add/` + post_id, commentDTO);
    }

    public Delete(comment_id: number) {
        return this.http.delete(`${environment.baseApiUrl}/${this.url}/delete/` + comment_id);
      }

}