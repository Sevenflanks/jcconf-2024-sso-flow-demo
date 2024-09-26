<template>
  <div>
    <div>
      <span>
        <button @click="hello">hello</button>
      </span>
    </div>
    <div>
      <h1 v-if="user.username">Hello {{ user.username }}, authorities: {{ user.authorities }}</h1>
      <h3 v-if="user.expireAt">Expire at {{ user.expireAt }}</h3>
    </div>

    <div>
      <span>
        <button @click="helloRole('twjug')">check twjug</button>
      </span>
    </div>
    <div>
      <h1 v-if="roleResp['twjug']">{{ roleResp['twjug'] }}</h1>
    </div>

    <div>
      <span>
        <button @click="helloRole('admin')">check admin</button>
      </span>
    </div>
    <div>
      <h1 v-if="roleResp['admin']">{{ roleResp['admin'] }}</h1>
    </div>

    <div>
      <span>
        <button @click="clear">clear all</button>
      </span>
    </div>
    <div>
      <span>
        <button @click="signout">signout</button>
      </span>
    </div>
  </div>
</template>

<script>
export default {
  data: () => ({
    user: {
      username: "",
      authorities: [],
      expireAt: "",
    },
    roleResp: {}
  }),
  methods: {
    async hello() {
      try {
        const resp = await fetch("/api/hello");
        if (resp.ok) {
          this.user = await resp.json();
        } else {
          throw new Error(`Response Error ${resp.status}: ${resp.statusText}`);
        }
      } catch (e) {
        alert(e);
      }
    },
    async helloRole(role) {
      try {
        const resp = await fetch(`/api/hello/role/${role}`);
        this.roleResp[role] = `Role:${role} check => ${resp.status}: ${resp.statusText}`;
      } catch (e) {
        alert(e);
      }
    },
    clear() {
      this.user = {
        username: "",
        authorities: [],
        expireAt: "",
      }
      this.roleResp = {}
    },
    async signout() {
      // window.location.assign("http://host.docker.internal:8081/realms/jcconf/protocol/openid-connect/logout?post_logout_redirect_uri=http://host.docker.internal:4180/oauth2/sign_out");
      window.location.assign("/oauth2/sign_out?" +
          "rd=http://host.docker.internal:8081/realms/jcconf/protocol/openid-connect/logout");
    }
  }
}
</script>

<style scoped>
button {
  margin: 5px;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: bold;
  font-family: 'Arial', sans-serif;
  background-color: #42b883;
  color: white;
  border: none;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  cursor: pointer;
  background-color: #2d8a69;
  transform: scale(1.05);
}

button:active {
  transform: scale(0.95);
}
</style>