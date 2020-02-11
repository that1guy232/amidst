package amidst.mojangapi.world.versionfeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import amidst.fragment.layer.LayerIds;
import amidst.mojangapi.minecraftinterface.RecognisedVersion;
import amidst.mojangapi.world.biome.Biome;
import amidst.mojangapi.world.icon.locationchecker.LocationChecker;
import amidst.mojangapi.world.icon.locationchecker.MineshaftAlgorithm_ChanceBased;
import amidst.mojangapi.world.icon.locationchecker.MineshaftAlgorithm_Original;
import amidst.mojangapi.world.icon.locationchecker.OceanMonumentLocationChecker_Fixed;
import amidst.mojangapi.world.icon.locationchecker.OceanMonumentLocationChecker_Original;
import amidst.mojangapi.world.icon.producer.CachedWorldIconProducer;
import amidst.mojangapi.world.icon.producer.StrongholdProducer_128Algorithm;
import amidst.mojangapi.world.icon.producer.StrongholdProducer_Buggy128Algorithm;
import amidst.mojangapi.world.icon.producer.StrongholdProducer_Original;
import amidst.mojangapi.world.oracle.BiomeDataOracle;

public enum DefaultVersionFeatures {
	;

	public static VersionFeatures create(RecognisedVersion version) {
		return FEATURES_BUILDER.create(version);
	}

	// @formatter:off
	private static final VersionFeatures.Builder FEATURES_BUILDER = VersionFeatures.builder()
			.with(FeatureKey.ENABLED_LAYERS, VersionFeature.<Integer> listBuilder()
				.init(
					LayerIds.ALPHA,
					LayerIds.BIOME_DATA,
					LayerIds.BACKGROUND,
					LayerIds.SLIME,
					LayerIds.GRID,
					LayerIds.SPAWN,
					LayerIds.STRONGHOLD,
					LayerIds.PLAYER,
					LayerIds.VILLAGE,
					LayerIds.MINESHAFT,
					LayerIds.NETHER_FORTRESS
				).sinceExtend(RecognisedVersion._12w21a,
					LayerIds.TEMPLE
				).sinceExtend(RecognisedVersion._1_8,
					LayerIds.OCEAN_MONUMENT
				).sinceExtend(RecognisedVersion._15w31c,
					LayerIds.END_ISLANDS,
					LayerIds.END_CITY
				).sinceExtend(RecognisedVersion._16w43a,
					LayerIds.WOODLAND_MANSION
				).sinceExtend(RecognisedVersion._18w09a,
					LayerIds.OCEAN_FEATURES
				).construct())
			.with(FeatureKey.VALID_BIOMES_FOR_STRUCTURE_SPAWN, VersionFeature.<Biome> listBuilder()
				.init(
					Biome.forest,
					Biome.plains,
					Biome.taiga,
					Biome.taigaHills,
					Biome.forestHills,
					Biome.jungle,
					Biome.jungleHills
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_STRONGHOLD, VersionFeature.<Biome> listBuilder()
				.init().since(RecognisedVersion._b1_8_1,
					Biome.desert,
					Biome.forest,
					Biome.extremeHills,
					Biome.swampland
				).sinceExtend(RecognisedVersion._b1_9_pre6,
					Biome.taiga,
					Biome.icePlains,
					Biome.iceMountains
				).sinceExtend(RecognisedVersion._1_1,
					Biome.desertHills,
					Biome.forestHills,
					Biome.extremeHillsEdge
				).sinceExtend(RecognisedVersion._12w03a,
					Biome.jungle,
					Biome.jungleHills
				).since(RecognisedVersion._13w36a,
					// this includes all the biomes above, except for the swampland
					getValidBiomesForStrongholdSinceV13w36a()
				).sinceExtend(RecognisedVersion._18w06a,
					Biome.mushroomIslandShore
				).construct())
			.with(FeatureKey.STRONGHOLD_PRODUCER_FACTORY, VersionFeature.<TriFunction<Long, BiomeDataOracle, List<Biome>, CachedWorldIconProducer>> builder()
				.init(
					(seed, biomeOracle, validBiomes) -> new StrongholdProducer_Original(seed, biomeOracle, validBiomes)
				).since(RecognisedVersion._15w43c,
					// this should be 15w43a, which is not recognised
					(seed, biomeOracle, validBiomes) -> new StrongholdProducer_Buggy128Algorithm(seed, biomeOracle, validBiomes)
				).since(RecognisedVersion._1_9_pre2,
					// this should be 16w06a
					(seed, biomeOracle, validBiomes) -> new StrongholdProducer_128Algorithm(seed, biomeOracle, validBiomes)
				).construct())
			.with(FeatureKey.VALID_BIOMES_FOR_STRUCTURE_VILLAGE, VersionFeature.<Biome> listBuilder()
				.init(
					Biome.plains,
					Biome.desert,
					Biome.savanna
				).sinceExtend(RecognisedVersion._16w20a,
					Biome.taiga
				).sinceExtend(RecognisedVersion._18w49a,
					Biome.icePlains
				).construct())
			.with(FeatureKey.VALID_BIOMES_FOR_STRUCTURE_PILLAGER_OUTPOST, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._18w47b,
					Biome.plains,
					Biome.desert,
					Biome.savanna,
					Biome.taiga
				).sinceExtend(RecognisedVersion._18w49a,
					Biome.icePlains
				).construct())
			.with(FeatureKey.DO_COMPLEX_VILLAGE_CHECK, VersionFeature.<Boolean> builder()
				.init(
					true
				).since(RecognisedVersion._16w20a,
					false
				).construct())
			.with(FeatureKey.OUTPOST_VILLAGE_AVOID_DISTANCE, VersionFeature.<Integer> builder()
				.init(
					-1
				// from 19w11a to 19w13a, outpost towers aren't generated close
				// to villages, but the structure is still reported by `/locate`.
				).since(RecognisedVersion._19w13b,
					10
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_DESERT_TEMPLE, VersionFeature.<Biome> listBuilder()
				.init(
					Biome.desert,
					Biome.desertHills
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_IGLOO, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._15w43c,
					Biome.icePlains,
					Biome.coldTaiga
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_JUNGLE_TEMPLE, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._12w22a,
					Biome.jungle
				).sinceExtend(RecognisedVersion._1_4_2,
					Biome.jungleHills // TODO: jungle temples spawn only since 1.4.2 in jungle hills?
				).sinceExtend(RecognisedVersion._19w06a,
					Biome.bambooJungle,
					Biome.bambooJungleHills
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_WITCH_HUT, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._1_4_2,
					Biome.swampland
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_OCEAN_RUINS, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._18w09a,
					Biome.ocean,
					Biome.deepOcean,
					Biome.coldOcean,
					Biome.coldDeepOcean,
					Biome.warmOcean,
					Biome.warmDeepOcean,
					Biome.lukewarmOcean,
					Biome.lukewarmDeepOcean,
					Biome.frozenOcean,
					Biome.frozenDeepOcean
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_SHIPWRECK, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._18w11a,
					Biome.beach,
					Biome.coldBeach,
					Biome.ocean,
					Biome.deepOcean,
					Biome.coldOcean,
					Biome.coldDeepOcean,
					Biome.warmOcean,
					Biome.warmDeepOcean,
					Biome.lukewarmOcean,
					Biome.lukewarmDeepOcean,
					Biome.frozenOcean,
					Biome.frozenDeepOcean
				).construct())
			.with(FeatureKey.MINESHAFT_ALGORITHM_FACTORY, VersionFeature.<Function<Long, LocationChecker>> builder()
				.init(
					(Long seed) -> new MineshaftAlgorithm_Original(seed)
				).since(RecognisedVersion._1_4_2,
					(Long seed) -> new MineshaftAlgorithm_ChanceBased(seed, 0.01D, true)
				).since(RecognisedVersion._1_7_2,
					(Long seed) -> new MineshaftAlgorithm_ChanceBased(seed, 0.004D, true)
				).since(RecognisedVersion._18w06a,
					(Long seed) -> new MineshaftAlgorithm_ChanceBased(seed, 0.01D, false)
				).construct())
			.with(FeatureKey.OCEAN_MONUMENT_LOCATION_CHECKER_FACTORY, VersionFeature.<QuadFunction<Long, BiomeDataOracle, List<Biome>, List<Biome>, LocationChecker>> builder()
				.init(
					(seed, biomeOracle, validCenterBiomes, validBiomes) -> new OceanMonumentLocationChecker_Original(seed, biomeOracle, validCenterBiomes, validBiomes)
				).since(RecognisedVersion._15w46a,
					(seed, biomeOracle, validCenterBiomes, validBiomes) -> new OceanMonumentLocationChecker_Fixed(seed, biomeOracle, validCenterBiomes, validBiomes)
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_OCEAN_MONUMENT, VersionFeature.<Biome> listBuilder()
				.init()
				.sinceExtend(RecognisedVersion._1_8,
					Biome.deepOcean,
					Biome.coldDeepOcean,
					Biome.warmDeepOcean,
					Biome.lukewarmDeepOcean,
					Biome.frozenDeepOcean
				).construct())
			.with(FeatureKey.VALID_BIOMES_AT_MIDDLE_OF_CHUNK_BURIED_TREASURE, VersionFeature.<Biome> listBuilder()
				.init().sinceExtend(RecognisedVersion._18w10d,
					Biome.beach,
					Biome.coldBeach
				).construct())
			.with(FeatureKey.VALID_BIOMES_FOR_STRUCTURE_OCEAN_MONUMENT, VersionFeature.<Biome> listBuilder()
				.init().sinceExtend(RecognisedVersion._1_8,
					Biome.ocean,
					Biome.deepOcean,
					Biome.frozenOcean,
					Biome.river,
					Biome.frozenRiver,
					Biome.coldOcean,
					Biome.coldDeepOcean,
					Biome.warmOcean,
					Biome.warmDeepOcean,
					Biome.lukewarmOcean,
					Biome.lukewarmDeepOcean,
					Biome.frozenDeepOcean
				).construct())
			.with(FeatureKey.VALID_BIOMES_FOR_STRUCTURE_WOODLAND_MANSION, VersionFeature.<Biome> listBuilder()
				.init(
					Biome.roofedForest,
					Biome.roofedForestM
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_DESERT_TEMPLE, VersionFeature.<Long> builder()
				.init(
					14357617L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_IGLOO, VersionFeature.<Long> builder()
				.init(
					14357617L
				).since(RecognisedVersion._18w06a,
					14357618L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_JUNGLE_TEMPLE,  VersionFeature.<Long> builder()
				.init(
						14357617L
				).since(RecognisedVersion._18w06a,
						14357619L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_WITCH_HUT, VersionFeature.<Long> builder()
				.init(
						14357617L
				).since(RecognisedVersion._18w06a,
						14357620L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_OCEAN_RUINS, VersionFeature.<Long> builder()
				.init(
						14357621L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_SHIPWRECK, VersionFeature.<Long> builder()
				.init(
						165745295L
				).construct())
			.with(FeatureKey.SEED_FOR_STRUCTURE_BURIED_TREASURE, VersionFeature.<Long> builder()
				.init(
						10387320L
				).construct())
			.with(FeatureKey.MAX_DISTANCE_SCATTERED_FEATURES_SHIPWRECK, VersionFeature.<Byte> builder()
				.init(
					(byte) 15
				).since(RecognisedVersion._1_13_pre7,
					(byte) 16
				).since(RecognisedVersion._20w06a,
					(byte) 24
				).construct())
			.with(FeatureKey.MIN_DISTANCE_SCATTERED_FEATURES_SHIPWRECK, VersionFeature.<Byte> builder()
				.init(
					(byte) 8
				).since(RecognisedVersion._20w06a,
					(byte) 4
				).construct())
			.with(FeatureKey.MAX_DISTANCE_SCATTERED_FEATURES_OCEAN_RUINS, VersionFeature.<Byte> builder()
				.init(
					(byte) 16
				).since(RecognisedVersion._20w06a,
					(byte) 20
				).construct())
			.with(FeatureKey.BUGGY_STRUCTURE_COORDINATE_MATH, VersionFeature.<Boolean> builder()
				.init(
						false
				).since(RecognisedVersion._18w06a,
						true  // Bug MC-131462.
				).since(RecognisedVersion._1_13_pre4,
						false
				).since(RecognisedVersion._1_13_pre7,
						true  // Bug MC-131462, again.
				).since(RecognisedVersion._18w30b,
						false
				).construct());

	private static List<Biome> getValidBiomesForStrongholdSinceV13w36a() {
		List<Biome> result = new ArrayList<>();
		for (Biome biome : Biome.allBiomes()) {
			if (biome.getType().getBiomeDepth() > 0) {
				result.add(biome);
			}
		}
		return result;
	}
}
