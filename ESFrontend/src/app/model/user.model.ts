export class User {
    user_id: number = 0;
    username: string = "";
    password: string = "";
    avatar: string = "";
    email: string = "";
    description: string = "";
    display_name: string = "";

    User(user_id: number, username: string, password: string, avatar: string, email: string, description: string, display_name: string) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.description = description;
        this.display_name = display_name;
    }
}