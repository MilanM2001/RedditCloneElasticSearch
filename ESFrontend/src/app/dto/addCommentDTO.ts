import { Flair } from "../model/flair.model";
import { Rule } from "../model/rule.model";

export class AddCommentDTO {
    text: string = "";

    AddCommentDTO(text: string) {
        this.text = text;
    }
}