package io.boonlogic.soul.land.web.backend

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/search")
@ResponseBody
class SearchService {


    @GetMapping("quick/{cardType}/{searchTerm}")
    fun quickSearch(@PathVariable("cardType") cardType: String,
                    @PathVariable("searchTerm") searchTerm: String,
                    @RequestParam("permutate", required = false) permutate: Boolean? = false) =

        when(cardType) {
            "rare" -> generateRare(permutate)
            "any" -> if (searchTerm == "empty") emptyList() else generateAny()
            else -> generateElse()
        }

    private fun generateRare(permutate: Boolean?) = if (permutate != null && permutate)
        listOf(
            Card(firstName = "rare", lastName = "rabbit", power = "eat"),
            Card(firstName = "rare", lastName = "hamster", power = "run"),
            Card(firstName = "medusa", lastName = "summoner", power = "sleep", power2 = "seduce"),
            Card(firstName = "icy", lastName = "dragon", power = "breath"),
            Card(firstName = "rare", lastName = "human", power = "eat")
        )
    else
        listOf(
            Card(firstName = "medusa", lastName = "summoner", power = "sleep", power2 = "seduce"),
            Card(firstName = "icy", lastName = "dragon", power = "breath"),
            Card(firstName = "rare", lastName = "human", power = "eat")
        )

    private fun generateAny() = listOf(
        Card(firstName = "any", lastName = "rabbit", power = "eat"),
        Card(firstName = "any", lastName = "hamster", power = "run"),
        Card(firstName = "medusa", lastName = "summoner", power = "sleep", power2 = "seduce"),
        Card(firstName = "any", lastName = "dragon", power = "breath"),
        Card(firstName = "any", lastName = "human", power = "eat")
    )

    private fun generateElse() = listOf(
        Card(firstName = "normal", lastName = "rabbit", power = "eat"),
        Card(firstName = "normal", lastName = "hamster", power = "run"),
        Card(firstName = "normal", lastName = "summoner", power = "sleep", power2 = "seduce"),
        Card(firstName = "normal", lastName = "dragon", power = "breath"),
        Card(firstName = "normal", lastName = "human", power = "eat")
    )

}


data class Card(val firstName: String? = null,
                val lastName: String? = null,
                val power: String? = null,
                val power2: String? = null,
                val power3: String? = null)
