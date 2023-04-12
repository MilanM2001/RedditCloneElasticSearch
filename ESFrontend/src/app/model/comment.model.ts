import { Reaction } from "./reaction.model";
import { User } from "./user.model";

export class Comment {
    comment_id: number = 0;
    text: string = ""
    user: User = new User();

    Comment(comment_id: number, text: string, user: User) {
        this.comment_id = comment_id;
        this.text = text;
        this.user = user;
    }
}