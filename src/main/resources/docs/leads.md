---
title: "Lead Capture & Forms"
description: "Handling asynchronous form submissions with server-side validation and HTMX partial swaps."
category: "Features & Plugins"
order: 1
badge: "HTMX"
---

AuraLaunch includes a fully implemented, AJAX-driven contact and lead capture form that requires zero JavaScript boilerplate.

## Form Architecture

The lead capture form uses HTMX attributes directly on the HTML `<form>` element:

```html
<form hx-post="/leads"
      hx-target="#form-container"
      hx-swap="outerHTML"
      hx-indicator="#loading-spinner"
      class="space-y-4">
  <input type="text" name="name" required ... />
  <input type="email" name="email" required ... />
  <textarea name="message" required ...></textarea>
  <button type="submit">Send Inquiry</button>
</form>
```

## Backend Validation & Controller

The form submission is handled by `LeadController` with standard Jakarta validation:

```kotlin
@PostMapping("/leads")
fun handleLead(
    @Valid @ModelAttribute request: LeadCaptureRequest,
    bindingResult: BindingResult,
    model: Model,
): String {
    if (bindingResult.hasErrors()) {
        model.addAttribute("errors", bindingResult.allErrors)
        return "fragments/forms :: contact-form"
    }

    leadService.processLead(request)
    return "fragments/forms :: contact-success"
}
```

:::tip CRM & Webhook Integration
You can extend `LeadService.kt` to forward captured leads to external services like Slack, Discord webhooks, Mailchimp, ConvertKit, or persist them into a PostgreSQL database.
:::
