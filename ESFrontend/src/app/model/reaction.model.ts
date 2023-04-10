import { Post } from "./post.model";
import { ReactionType } from "./reactionType.enum";
import { User } from "./user.model";

export class Reaction {
    public reaction_id: number = 0;
    public reaction_type: ReactionType = ReactionType.UPVOTE;
    public timestamp: Date = new Date();
    public user: User = new User;
    public comment: Comment = new Comment;
    public post: Post = new Post;

    constructor() {
        this.reaction_id = 0;
        this.reaction_type = ReactionType.UPVOTE;
        this.timestamp = new Date();
        this.user = new User;
        this.comment = new Comment;
        this.post = new Post;
    }

    Reaction(reaction_id: number, reaction_type: ReactionType, timestamp: Date, user: User, comment: Comment, post: Post) {
        this.reaction_id = reaction_id;
        this.reaction_type = reaction_type;
        this.timestamp = timestamp;
        this.user = user;
        this.comment = comment;
        this.post = post;
    }
}