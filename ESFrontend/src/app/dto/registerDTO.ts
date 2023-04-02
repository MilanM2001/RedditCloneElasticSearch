export class RegisterDTO {
    username: string = "";
    password: string = "";
    email: string = "";
    display_name: string = "";

    RegisterDTO(username: string, password: string, email: string, display_name: string) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.display_name = display_name;
    }
}