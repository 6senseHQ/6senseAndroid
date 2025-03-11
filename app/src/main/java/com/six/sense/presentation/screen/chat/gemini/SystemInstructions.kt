package com.six.sense.presentation.screen.chat.gemini

/**
 * Enum class representing different system instructions tailored for specific roles.
 * Each enum value holds an instruction string and a role description.
 *
 * These instructions are designed to guide the system in adopting a specific tone,
 * language, and focus when interacting with users based on their profession or role.
 * They provide a way to customize the system's response style to better meet the
 * expectations and needs of different user personas.
 *
 * @property instruction The specific instruction string to guide the system's response style.
 * @property role The descriptive name of the role associated with the instruction.
 */
enum class SystemInstructions(val instruction: String, val role: String) {
    /**
    * Represents the default state or an unspecified value.
    * Often used when no specific value is provided or applicable.
     */
    DEFAULT("", ""),

    /**
     * Represents a technical individual, such as a developer, engineer, or IT specialist.
     * They prefer technical terminology, data-driven insights, and in-depth explanations.
     * They are also able to assist with troubleshooting and technology integrations.
     */
    TECHNICAL_PERSON(
        instruction = "Use technical terminology and precise language relevant to their domain, Provide data-driven insights and concrete examples, Avoid oversimplifications; these individuals often prefer a deeper understanding & be ready to assist with troubleshooting, research, or technology integration.",
        role = "Technical Person"
    ),
    ACCOUNTANT(
        instruction = "Focus on clarity and accuracy in financial terminology,Provide calculations, tax laws, or regulatory updates when necessary, Offer actionable steps for budget planning, audits, or compliance Be concise, as they value structured and factual responses.",
        role = "Accountant"
    ),

    /**
     * PROJECT_MANAGER entry in the enumeration.
     * This role focuses on the planning, execution, and delivery of projects.
     * It emphasizes clarity, timelines, and deliverables, and leverages project
     * management frameworks. The PROJECT_MANAGER is also responsible for team
     * coordination, risk management, and efficient resource allocation.
     */
    PROJECT_MANAGER(
        instruction = "Emphasize clarity, timelines, and deliverables. Use project management frameworks (e.g., Agile, Scrum, Waterfall) when applicable. Focus on team coordination, risk management, and resource allocation. Present information with a focus on outcomes and efficiency.",
        role = "Project Manager"
    ),

    /**
     * MARKETING_SPECIALIST entry in the enumeration.
     */
    MARKETING_SPECIALIST(
        instruction = "Use engaging and persuasive language. Focus on customer behavior, market trends, and ROI strategies. Offer creative ideas for campaigns, content, or branding initiatives. Provide examples or case studies whenever possible.",
        role = "Marketing Specialist"
    )
}