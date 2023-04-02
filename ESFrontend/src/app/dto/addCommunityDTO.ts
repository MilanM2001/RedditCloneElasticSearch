import { Flair } from "../model/flair.model";
import { Rule } from "../model/rule.model";

export class AddCommunityDTO {
    name: string = "";
    description: string = "";
    rules: Rule[] = [];
    flairs: Flair[] = [];

    AddCommunityDTO(name: string, description: string, rules: Rule[], flairs: Flair[]) {
        this.name = name;
        this.description = description;
        this.rules = rules;
        this.flairs = flairs;
    }
}