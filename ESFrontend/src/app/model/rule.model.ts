export class Rule {
    rule_id: number = 0;
    name: string = "";
    description: string = "";

    Rule(rule_id: number, name: string, description: string) {
        this.rule_id = rule_id;
        this.name = name;
        this.description = description;
    }
}