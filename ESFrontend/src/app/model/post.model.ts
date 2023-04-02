import { Community } from "./community.model";
import { Flair } from "./flair.model";
import { User } from "./user.model";

export class Post {
    post_id: number = 0;
    title: string = "";
    text: string = "";
    createdDate: Date = new Date('2022-05-03');
    community: Community = new Community;
    user: User = new User;
    flair: Flair = new Flair;

    Post(post_id: number, title: string, text: string, createdDate: Date, community: Community, user: User,flair: Flair) {
        this.post_id = post_id;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.community = community;
        this.user = user;
        this.flair = flair;
    }
}