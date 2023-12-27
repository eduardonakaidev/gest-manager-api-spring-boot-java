package br.com.nestworld.gestmanagerFinancyProductApi.modules.Store.enuns;

public enum RolesEntity {
    ADMIN("admin"),
    USER("user");

    private String role;

    RolesEntity(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
