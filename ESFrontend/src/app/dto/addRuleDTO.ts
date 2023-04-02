export class AddRuleDTO {
    name: string = "";
    description: string = "";

    AddRuleDTO(name: string, description: string) {
        this.name = name;
        this.description = description;
    }
}