package model;

public class PokemonWater extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonWater( String name, int maxHealth, int attack ) {
        super( name, PokemonType.WATER, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        switch( targetType ) {
            case ELECTRIC: return 1.0;
            case GRASS: return 0.5;
            case GROUND: return 1.5;
            case WATER: return 1.0;
            default: return 1.0;
        }
    }

    @Override
    public double getTypeAbility() {
        return 1.0;
    }

    // LEMBRANDO QUE A VANTAGEM DO POKEMON DE ÁGUA É EM RELAÇÃO AO TERRENO. DEVE SER MODIFICADO DEPOIS.
    // Override pra aplicar a habilidade defensiva dos pokémons tipo ÁGUA.
    @Override
    public void takeDamage( int damage ) {
        int reducedDamage = (int) ( damage * 0.75);
        super.takeDamage( reducedDamage );
    }
}


