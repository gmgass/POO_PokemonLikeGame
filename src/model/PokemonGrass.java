package model;

import java.util.Random;

public class PokemonGrass extends Pokemon {

    // ----- CONSTRUTOR -----
    public PokemonGrass( String name, int maxHealth, int attack ) {
        super( name, PokemonType.GRASS, maxHealth, attack );
    }

    @Override
    public double getTypeAdvantage( Pokemon target ) {
        PokemonType targetType = target.getType();

        return switch ( targetType ) {
            case ELECTRIC -> 1.0;
            case GRASS -> 1.0;
            case GROUND -> 1.5;
            case WATER -> 1.5;
            default -> 1.0;
        };
    }

    @Override
    public String toString() {
        return this.name + " Tipo: Grama";
    }

    // -----------------------------------------------------------------------------------------
    // HABILIDADE: CURA
    // 10% de chance de curar até 100% do dano causado.
    // 40% de chance de curar até 66% do dano causado.
    // 50% de chance de curar até 33% do dano causado.
    // -----------------------------------------------------------------------------------------
    @Override
    public double getAbilityDamage( Pokemon target ) {
        return 1.0;
    }

    @Override
    public void applyAbilityEffect( Pokemon target, int damage ) {
        if ( damage == 0 ) return;

        Random random = new Random();
        int roll = random.nextInt( 5 );
        int healAmount = 0;

        switch ( roll ) {
            case 0:     { healAmount = random.nextInt( damage ); } break;
            case 1, 2:  { healAmount = (int) random.nextInt( damage * 2/3 ); } break;
            case 3, 4:  { healAmount = (int) random.nextInt( damage * 1/3 ); } break;
        }

        this.health += healAmount;
        if ( this.health > this.maxHealth ) {
            this.health = this.maxHealth;
        }
    }
}


