package com.px8042.pxsyringe.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public final class PotionSyringeItem extends Item {
    private final String namedTranslationKey;
    private final boolean consumable;
    private final boolean infiniteDuration;

    public PotionSyringeItem(
            Properties properties,
            String namedTranslationKey,
            boolean consumable,
            boolean infiniteDuration
    ) {
        super(properties);
        this.namedTranslationKey = namedTranslationKey;
        this.consumable = consumable;
        this.infiniteDuration = infiniteDuration;
    }

    @Override
    public Component getName(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents == null) {
            return super.getName(stack);
        }

        Component potionName = contents.getName("item.minecraft.potion.effect.");
        return Component.translatable(namedTranslationKey, potionName);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        return inject(level, player, player, player.getItemInHand(hand));
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        if (!target.isAlive()) {
            return InteractionResult.PASS;
        }

        LivingEntity recipient = player.isSecondaryUseActive() ? target : player;
        return inject(player.level(), player, recipient, stack);
    }

    private InteractionResult inject(Level level, Player injector, LivingEntity target, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            float durationScale = stack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F);

            contents.forEachEffect(effect -> {
                if (effect.getEffect().value().isInstantenous()) {
                    effect.getEffect().value().applyInstantenousEffect(
                            serverLevel,
                            injector,
                            injector,
                            target,
                            effect.getAmplifier(),
                            1.0D
                    );
                } else {
                    target.addEffect(infiniteDuration ? withInfiniteDuration(effect) : effect, injector);
                }
            }, durationScale);

            serverLevel.playSound(
                    null,
                    target.getX(),
                    target.getY() + target.getBbHeight() * 0.5D,
                    target.getZ(),
                    SoundEvents.BOTTLE_EMPTY,
                    SoundSource.PLAYERS,
                    0.45F,
                    1.35F + serverLevel.getRandom().nextFloat() * 0.1F
            );
            injector.awardStat(Stats.ITEM_USED.get(this));
            if (consumable) {
                stack.consume(1, injector);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private static MobEffectInstance withInfiniteDuration(MobEffectInstance effect) {
        return new MobEffectInstance(
                effect.getEffect(),
                MobEffectInstance.INFINITE_DURATION,
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.isVisible(),
                effect.showIcon()
        );
    }
}
