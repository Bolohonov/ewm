<template>
  <div className="hello">
    <h1>DefaultPage + {{eventId}}</h1>
  </div>
</template>

<script>
export default {
  name: "EventInfo",
  props: ["eventId"],
}
</script>
