import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { CreateReactionDTO } from "../dto/createReactionDTO";
import { Reaction } from "../model/reaction.model";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ReactionService {
    private url = "reactions";
    constructor(private http: HttpClient) { }

    postKarma(post_id: number) {
        return this.http.get<number>(`${environment.baseApiUrl}/${this.url}/postKarma/` + post_id)
    }

    create(reaction: CreateReactionDTO) {
        return this.http.post<Reaction>(`${environment.baseApiUrl}/${this.url}/create`, reaction);
    }
}