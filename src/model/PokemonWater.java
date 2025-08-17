package model;

public class PokemonWater extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonWater( String name, int maxHealth, int attack ) {
        super( name, PokemonType.WATER, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case ELECTRIC -> 1.0;
            case GRASS -> 0.5;
            case GROUND -> 1.5;
            case WATER -> 1.0;
            default -> 1.0;
        };
    }

    @Override
    public String toString() {
        return this.name + " Tipo: Água";
    }

    // LEMBRANDO QUE A VANTAGEM DO POKEMON DE ÁGUA É EM RELAÇÃO AO TERRENO. DEVE SER MODIFICADO DEPOIS.
    @Override
    public double getAbilityDamage( Pokemon target ) {
        return 1.0;
    }

    @Override
    public void applyAbilityEffect( Pokemon target, int damage ) {}

    // Override pra aplicar a habilidade defensiva dos pokémons tipo ÁGUA.
    @Override
    public void takeDamage( int damage ) {
        int reducedDamage = (int) ( damage * 0.75);
        super.takeDamage( reducedDamage );
    }
}


