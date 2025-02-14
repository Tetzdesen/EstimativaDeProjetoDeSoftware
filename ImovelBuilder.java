public abstract class ImovelBuilder {
    protected Componente imovel;

    public abstract addSala();
    public abstract addAreaComum();    
    public abstract addQuartos();        
    public abstract addBanheiros();            
    public abstract addAreaSocial();                

    public final Componente getImovel() {
        return this.imovel;
    }
}

public class KitnetPequenaBuilder extends ImovelBuilder {

    public  addSala() {

    }

    public  addAreaComum() {
        imovel = new Imovel("kitnet pequena", 20);
    }

    public  addQuartos() {
        imovel = new Ambiente(imovel, "Quarto", 12);
    }
    public  addBanheiros(){

    }
           
    public  addAreaSocial(){

    }
}


public class KitnetLuxoBuilder extends ImovelBuilder {

    public  addSala() {}

    public  addAreaComum() {
        imovel = new Imovel("kitnet luxo", 50);
    }

    public  addQuartos() {
        imovel = new Ambiente(imovel, "Suite", 20);
    }
    public  addBanheiros(){}
           
    public  addAreaSocial(){
        imovel = new Ambiente(imovel, "Varanda com piscina", 15);
    }
}




public class DuplexLuxoBuilder extends ImovelBuilder {

    public  addSala() {}

    public  addAreaComum() {
        imovel = new Imovel("Duplex luxo", 60);
    }

    public  addQuartos() {
        imovel = new Ambiente(imovel, "Suite master primeiro andar", 20);
        imovel = new Ambiente(imovel, "Quarto primeiro andar", 20);
        imovel = new Ambiente(imovel, "Suite presidencial 2 andar", 30);
        imovel = new Ambiente(imovel, "Quarto 1 - 2 andar", 20);
        imovel = new Ambiente(imovel, "Quarto 2 - 2 andar", 20);
    }
    public  addBanheiros(){}
           
    public  addAreaSocial(){
        imovel = new Ambiente(imovel, "Sala de estar", 30);
        imovel = new Ambiente(imovel, "Varanda gourmet", 20);
        imovel = new Ambiente(imovel, "Cinema", 35);
        imovel = new Ambiente(imovel, "Cobertura com piscina e area de churrasco", 150);

    }
}


public class Diretor {

    public static Componente build(ImovelBuilder builder) {

        builder.addSala();
        builder.addAreaComum();    
        builder.addQuartos();        
        builder.addBanheiros();            
        builder.addAreaSocial();                
        return builder.getImovel();

    }
}

public class Principal {
    public static void main(String args[]) {


        Componente imovel = Diretor.build(new DuplexLuxoBuilder());

        System.out.println(imovel);

    }
}