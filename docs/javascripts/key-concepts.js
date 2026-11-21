/* Key Concepts: the Read / Test yourself switch, turning cards over,
   and highlighting a card when you arrive at it from the map. */
(function () {
  "use strict";

  function setUpToolbar(bar) {
    var page = bar.closest("article") || document.body;
    var cards = Array.prototype.slice.call(page.querySelectorAll(".kc-card"));
    var progress = bar.querySelector(".kc-progress");
    var reset = bar.querySelector(".kc-reset");
    var modes = bar.querySelectorAll(".kc-mode");

    function testing() {
      return page.classList.contains("kc-testing");
    }

    function update() {
      var turned = cards.filter(function (c) {
        return c.classList.contains("is-flipped");
      }).length;
      progress.textContent = turned + " of " + cards.length + " turned";
    }

    function setMode(mode) {
      page.classList.toggle("kc-testing", mode === "test");
      Array.prototype.forEach.call(modes, function (b) {
        var on = b.getAttribute("data-mode") === mode;
        b.classList.toggle("is-active", on);
        b.setAttribute("aria-pressed", on ? "true" : "false");
      });
      cards.forEach(function (c) {
        c.classList.remove("is-flipped");
      });
      progress.hidden = reset.hidden = mode !== "test";
      update();
    }

    function turn(card) {
      card.classList.toggle("is-flipped");
      update();
    }

    cards.forEach(function (card) {
      card.addEventListener("click", function (e) {
        if (!testing() || e.target.closest("a")) return;
        turn(card);
      });
      card.addEventListener("keydown", function (e) {
        if (!testing()) return;
        if (e.key === "Enter" || e.key === " ") {
          e.preventDefault();
          turn(card);
        }
      });
    });

    Array.prototype.forEach.call(modes, function (b) {
      b.addEventListener("click", function () {
        setMode(b.getAttribute("data-mode"));
      });
    });
    reset.addEventListener("click", function () {
      cards.forEach(function (c) {
        c.classList.remove("is-flipped");
      });
      update();
    });
  }

  function highlightTarget() {
    var id = decodeURIComponent(location.hash.slice(1));
    if (id.indexOf("kc-") !== 0) return;
    var heading = document.getElementById(id);
    var card = heading && heading.closest(".kc-card");
    if (!card) return;
    card.classList.add("is-target");
    setTimeout(function () {
      card.classList.remove("is-target");
    }, 1800);
  }

  function init() {
    Array.prototype.forEach.call(document.querySelectorAll(".kc-toolbar"), setUpToolbar);
    highlightTarget();
  }

  window.addEventListener("hashchange", highlightTarget);
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
