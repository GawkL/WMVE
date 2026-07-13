package ru.tempelstudio.WMVE.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.ParseResults;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(value = CommandDispatcher.class, remap = false)
public class CommandDispatcherMixin {

    @Inject(method = "parse(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)Lcom/mojang/brigadier/ParseResults;", at = @At("HEAD"))
    private void normalizeModCommandCase(StringReader reader, Object source, CallbackInfoReturnable<ParseResults<?>> cir) {
        String remainingText = reader.getRemaining();
        String lowerText = remainingText.toLowerCase(Locale.ROOT);
        if (lowerText.startsWith("wmve ") || lowerText.equals("wmve")) {
            int cursor = reader.getCursor();
            String fullString = reader.getString();
            String prefix = fullString.substring(0, cursor);
            String normalizedRemaining = remainingText.toLowerCase(Locale.ROOT);
            String newString = prefix + normalizedRemaining;
            try {
                java.lang.reflect.Field stringField = StringReader.class.getDeclaredField("string");
                stringField.setAccessible(true);
                stringField.set(reader, newString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
