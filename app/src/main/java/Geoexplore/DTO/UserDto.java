package Geoexplore.DTO;

public class UserDto {

    private String nome;
    private String cognome;
    private String email;

    // Costruttore vuoto
    public UserDto() {
    }

    // Costruttore completo
    public UserDto(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    // Getter e Setter per 'nome'
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter per 'cognome'
    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    // Getter e Setter per 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
