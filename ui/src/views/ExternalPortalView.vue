<script setup>

</script>

<template>
  <form action="http://host.docker.internal:9000/external-login" method="post">
    <input name="digest" type="text" value="valid-token"/>
    <button type="submit">Login</button>
  </form>
</template>

<style scoped>

</style>
