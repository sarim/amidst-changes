package amidst.mojangapi.world.icon.locationchecker;

import java.util.List;

import amidst.documentation.ThreadSafe;
import amidst.mojangapi.world.biome.Biome;
import amidst.mojangapi.world.oracle.BiomeDataOracle;

@ThreadSafe
public class WoodlandMansionLocationChecker extends AllValidLocationChecker {
	private static final long STRUCTURE_SALT = 10387319L;
	private static final byte MAX_DISTANCE_BETWEEN_SCATTERED_FEATURES = 80;
	private static final byte MIN_DISTANCE_BETWEEN_SCATTERED_FEATURES = 20;
	private static final boolean USE_TWO_VALUES_FOR_UPDATE = true;
	private static final int STRUCTURE_SIZE = 32;

	public WoodlandMansionLocationChecker(long seed, BiomeDataOracle biomeDataOracle, List<Biome> validBiomesForStructure) {
		super(
				new StructureAlgorithm(
						seed,
						STRUCTURE_SALT,
						MAX_DISTANCE_BETWEEN_SCATTERED_FEATURES,
						MIN_DISTANCE_BETWEEN_SCATTERED_FEATURES,
						USE_TWO_VALUES_FOR_UPDATE),
				new StructureBiomeLocationChecker(biomeDataOracle, STRUCTURE_SIZE, validBiomesForStructure));
	}
}
