package com.engineerfred.reclaim.core.domain.model

enum class AddictionCategory {
    // Behavioral
    PORNOGRAPHY,
    GAMBLING,
    BINGE_EATING,
    GAMING,
    SHOPPING,
    SELF_HARM,

    // Substance
    ALCOHOL,
    COCAINE,
    MARIJUANA,
    NICOTINE,
    PRESCRIPTION_DRUGS,

    // Digital
    SOCIAL_MEDIA,
    PHONE,
    STREAMING;

    fun toAddictionType(): AddictionType = when (this) {
        PORNOGRAPHY, GAMBLING, BINGE_EATING,
        GAMING, SHOPPING, SELF_HARM -> AddictionType.BEHAVIORAL

        ALCOHOL, COCAINE, MARIJUANA,
        NICOTINE, PRESCRIPTION_DRUGS -> AddictionType.SUBSTANCE

        SOCIAL_MEDIA, PHONE,
        STREAMING -> AddictionType.DIGITAL
    }

    fun displayName(): String = when (this) {
        PORNOGRAPHY       -> "Pornography / Masturbation"
        GAMBLING          -> "Gambling"
        BINGE_EATING      -> "Binge Eating"
        GAMING            -> "Gaming Addiction"
        SHOPPING          -> "Shopping Addiction"
        SELF_HARM         -> "Self-Harm Urges"
        ALCOHOL           -> "Alcohol"
        COCAINE           -> "Cocaine / Hard Drugs"
        MARIJUANA         -> "Marijuana"
        NICOTINE          -> "Nicotine / Vaping"
        PRESCRIPTION_DRUGS -> "Prescription Drug Abuse"
        SOCIAL_MEDIA      -> "Social Media Addiction"
        PHONE             -> "Phone / Screen Addiction"
        STREAMING         -> "Doom-Scrolling / Streaming"
    }
}