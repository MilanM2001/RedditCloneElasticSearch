export class CreateReactionDTO {
    public reaction_type: string;

    public post_id: number;

    public comment_id: number;

    constructor() {
        this.reaction_type = "";
        this.post_id = 0;
        this.comment_id = 0;
    }
}