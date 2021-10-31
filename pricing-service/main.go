package main

import (
	"fmt"
	"log"
	"net/http"
)

func handleInfo(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "I am pricing microservice")
}

func handleRequests() {
	http.HandleFunc("/info", handleInfo)
	log.Fatal(http.ListenAndServe(":8081", nil))
}

func main() {
	handleRequests()
}
