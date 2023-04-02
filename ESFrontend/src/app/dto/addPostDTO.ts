export class AddPostDTO {
    title: string = "";
    text: string = "";
    flair_id: number = 0;

    AddPostDTO(title: string, text: string, flair_id: number) {
        this.title = title;
        this.text = text;
        this.flair_id = flair_id;
    }
}